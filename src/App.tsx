import './App.css';
import Header from './components/Header/Header';
import { DropzoneButton } from './components/Header/DropzoneButton';
import { FileUploadItem } from './components/Header/FileUploadItem';
import { useState } from 'react';
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
            {files.map((file, index) => (
              <FileUploadItem
                key={`${file.name}-${index}`}
                file={file}
                onBlankPage={false}
                onRemove={() => setFiles(prev => prev.filter((_, i) => i !== index))}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
