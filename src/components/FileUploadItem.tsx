import { Text, Progress, Group } from '@mantine/core';

interface FileUploadItemProps {
  file: File;
  status: 'pending' | 'uploading' | 'complete' | 'error' ;
  progress?: number; // optional 0–100
}

export function FileUploadItem({ file, status, progress = 0 }: FileUploadItemProps) {
  return (
    <Group
      justify="space-between"
      align="center"
      p="sm"
      mt="sm"
      style={{
        border: '1px solid #ddd',
        borderRadius: 8,
      }}
    >
      <div>
        <Text fw={500}>{file.name}</Text>
        <Text size="xs" c="dimmed">
          {(file.size / 1024).toFixed(1)} KB
        </Text>
        <Text size="xs">
          Status: {status}
        </Text>
      </div>

      {status === 'uploading' && (
        <Progress value={progress} w={120} />
      )}

      {status === 'complete' && (
        <Text c="#4da8f7" fw={500}>File Uploaded</Text>
      )}

      {status === 'error' && (
        <Text c="red" fw={500}>Failed ❌</Text>
      )}
    </Group>
  );
}
