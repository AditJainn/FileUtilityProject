import './App.css';
import Header from './components/Header/Header';
import { DropzoneButton } from './components/DropzoneButton';
import { useState } from 'react';
import FileUpload from './components/fileUpload';


function App() {
  const [files, setFiles] = useState<File[]>([]);

  const handleFilesAdded = (newFiles: File[]) => {
    setFiles(prev => [...prev, ...newFiles]);
  };

  return (
    <div className="App">
      <Header />

      <div style={{ marginTop: 20 }}>
        <DropzoneButton onFilesAdded={handleFilesAdded} />

        {files.length > 0 && (
          <div style={{ marginTop: 20 }}>
            <h3>Files Uploaded:</h3>
            <ul>
              {files.map((file, index) => (
                <li key={index}>{file.name}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
