import React, { useState } from 'react';
import Editor from '@monaco-editor/react';

const LANGUAGES = [
  { id: 'python', name: 'Python', defaultCode: "print('Hello from Polyglot Python!')" },
  { id: 'js', name: 'JavaScript', defaultCode: "console.log('Hello from Polyglot JavaScript!');" },
  { id: 'ruby', name: 'Ruby', defaultCode: "puts 'Hello from Polyglot Ruby!'" }
];

export default function App() {
  const [activeLang, setActiveLang] = useState(LANGUAGES[0]);
  const [codeSnippets, setCodeSnippets] = useState({
    python: LANGUAGES[0].defaultCode,
    js: LANGUAGES[1].defaultCode,
    ruby: LANGUAGES[2].defaultCode
  });
  const [output, setOutput] = useState('Click "Run Code" to see the output here...');
  const [loading, setLoading] = useState(false);

  const handleEditorChange = (value) => {
    setCodeSnippets(prev => ({ ...prev, [activeLang.id]: value }));
  };

  const runCode = async () => {
    setLoading(true);
    setOutput('Executing inside GraalVM JVM sandbox...');
    try {
      const response = await fetch('http://localhost:8080/api/polyglot/run', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          language: activeLang.id,
          code: codeSnippets[activeLang.id]
        }),
      });

      const resultText = await response.text();
      setOutput(resultText);
    } catch (error) {
      setOutput('Error connecting to backend: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', background: '#1e1e1e', color: '#fff', fontFamily: 'sans-serif' }}>

      {/* Top Bar / Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', background: '#2d2d2d', padding: '10px 20px', borderBottom: '1px solid #333' }}>
        <div style={{ display: 'flex', gap: '10px' }}>
          {LANGUAGES.map(lang => (
            <button
              key={lang.id}
              onClick={() => setActiveLang(lang)}
              style={{
                padding: '8px 16px',
                background: activeLang.id === lang.id ? '#0e639c' : '#3c3c3c',
                color: '#fff',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontWeight: 'bold'
              }}
            >
              {lang.name}
            </button>
          ))}
        </div>
        <button
          onClick={runCode}
          disabled={loading}
          style={{
            padding: '8px 20px',
            background: '#107c10',
            color: '#fff',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
            fontWeight: 'bold'
          }}
        >
          {loading ? 'Running...' : '▶ Run Code'}
        </button>
      </div>

      {/* Main Split Area */}
      <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
        <div style={{ flex: 3, height: '100%' }}>
          <Editor
            height="100%"
            language={activeLang.id === 'js' ? 'javascript' : activeLang.id}
            theme="vs-dark"
            value={codeSnippets[activeLang.id]}
            onChange={handleEditorChange}
            options={{ fontSize: 14, minimap: { enabled: false } }}
          />
        </div>

        <div style={{ flex: 1, background: '#111', borderLeft: '1px solid #333', display: 'flex', flexDirection: 'column' }}>
          <div style={{ background: '#222', padding: '10px', fontSize: '12px', color: '#888', borderBottom: '1px solid #333', fontWeight: 'bold' }}>
            CONSOLE OUTPUT
          </div>
          <pre style={{ padding: '15px', margin: 0, color: '#4ec9b0', fontFamily: 'monospace', fontSize: '13px', whiteSpace: 'pre-wrap', wordBreak: 'break-all', overflowY: 'auto', flex: 1 }}>
            {output}
          </pre>
        </div>
      </div>

    </div>
  );
}