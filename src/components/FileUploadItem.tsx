import { useState } from 'react';
import { Text, Button, Progress, Group } from '@mantine/core';

export type UploadStatus = 'pending' | 'uploading' | 'complete' | 'error';

interface FileUploadItemProps {
  file: File;
  onBlankPage?: boolean;
  onRemove:() => void;
}
type SetProgress = React.Dispatch<React.SetStateAction<number>>;

function runSimulation(setProgress: SetProgress): Promise<void> {
  return new Promise((resolve) => {
    // Start the simulation immediately
    const interval = setInterval(() => {
      setProgress((p) => {
        if (p >= 90) {
          clearInterval(interval);
          resolve(); 
          return 90; 
        }
        return p + 10;
      });
    }, 50); 
  });
}

export function FileUploadItem({ file, onBlankPage = false, onRemove }: FileUploadItemProps) {
  const [status, setStatus] = useState<UploadStatus>('pending');
  const [progress, setProgress] = useState(0);
  const [downloadUrl, setDownloadUrl] = useState<string | null>(null);

  const handleUpload = async () => {
    setStatus('uploading');
    setProgress(0);

    const formData = new FormData();
    formData.append('file', file);
    formData.append('onBlankPage', String(onBlankPage));
        
    const fetchPromise = fetch('http://localhost:8080/upload', {
      method: 'POST',
      body: formData,
    }).then(response => {
      // This part runs once the network request is complete and headers are received.
      if (!response.ok) {
        // Throw an error to be caught by the try/catch block
        throw new Error(`Upload failed with status: ${response.status}`);
      }
      return response;
    });

    const simulationPromise = runSimulation(setProgress);

    try {

      const [response] = await Promise.all([fetchPromise, simulationPromise]);

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      setDownloadUrl(url);

      setProgress(100);
      setStatus('complete');
      
    } catch (err) {
      console.error(err);
      setStatus('error');
      setProgress(0); // Reset progress
    }
  };

  const handleDownload = () => {
    if (!downloadUrl) return;
    const a = document.createElement('a');
    a.href = downloadUrl;
    a.download = file.name.replace(/\.[^/.]+$/, '') + '.pdf'; 
    a.click();
  };

  const handleCancel = () => {
    setStatus('pending');
    setProgress(0);
    if (downloadUrl) {
      window.URL.revokeObjectURL(downloadUrl);
      setDownloadUrl(null);
    }
    onRemove();
  };

  return (
    <Group
      justify="space-between"
      align="center"
      p="sm"
      mt="sm"
      style={{ border: '1px solid #ddd', borderRadius: 8, width: '100%' }}
    >
      <div>
        <Text fw={500}>{file.name}</Text>
        <Text size="xs" ta="left" c="dimmed">{(file.size / 1024).toFixed(1)} KB</Text>
        <Text size="xs" ta="left" >Status: {status}</Text>
        {status === 'uploading' && <Progress ta="left" value={progress} mt="xs" w={150} />}
      </div>

      <div style={{ display: 'flex', gap: 8 }}>
        {status === 'pending' && <Button size="xs" onClick={handleUpload}>Upload</Button>}
        {status !== 'uploading' && status !== 'complete' && (
          <Button size="xs" color="red" onClick={handleCancel}>Cancel</Button>
        )}
        {status === 'complete' && downloadUrl && (
          <Button size="xs" color="blue" onClick={handleDownload}>Download PDF</Button>
        )}
        {status === 'error' && <Text color="red">Failed ❌</Text>}
      </div>
    </Group>
  );
}