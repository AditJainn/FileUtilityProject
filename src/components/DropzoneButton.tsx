import { useRef, useState } from 'react';

import { IconCloudUpload, IconDownload, IconX } from '@tabler/icons-react';
import { Button, Group, Text, useMantineTheme } from '@mantine/core';
import { Dropzone, MIME_TYPES } from '@mantine/dropzone';
import classes from './DropzoneButton.module.css';
import { FileUploadItem } from './FileUploadItem';


type UploadFile = {
  file: File;
  status: 'pending' | 'uploading' | 'complete' | 'error';
  progress: number;

};
export function DropzoneButton() {
  const theme = useMantineTheme();
  const openRef = useRef<() => void>(null);
  const acceptedFiles = ".jpg / jpeg /  png";
  const [uploads, setUploads] = useState<UploadFile[]>([]);

  const startFakeUpload = (index: number) => {
    setUploads((prev) =>
      prev.map((item, i) =>
        i === index ? { ...item, status: 'uploading' } : item
      )
    );
    let progress = 0;

    const interval = setInterval(() => {
      progress += 10;

      setUploads((prev) =>
        prev.map((item, i) =>
          i === index ? { ...item, progress } : item
        )
      );

      if (progress >= 100) {
        clearInterval(interval);
        setUploads((prev) =>
          prev.map((item, i) =>
            i === index
              ? { ...item, status: 'complete', progress: 100 }
              : item
          )
        );
      }
    }, 300);
  }

  return (
    <div className={classes.wrapper}>
      <Dropzone
        openRef={openRef}
        onDrop={(acceptedFiles) => {
          const newUploads = acceptedFiles.map((file) => ({
            file,
            status: 'pending' as const,
            progress: 0,
          }));

          setUploads((prev) => [...prev, ...newUploads]);
          newUploads.forEach((_, i) =>
            startFakeUpload(uploads.length + i)
          );
          console.log('Accepted files:', acceptedFiles);
        }}

        className={classes.dropzone}
        radius="md"
        accept={[MIME_TYPES.png, MIME_TYPES.jpeg]}
        maxSize={30 * 1024 ** 2}
      >
        <div style={{ pointerEvents: 'none' }}>
          <Group justify="center">
            <Dropzone.Accept>
              <IconDownload size={50} color={theme.colors.blue[6]} stroke={1.5} />
            </Dropzone.Accept>
            <Dropzone.Reject>
              <IconX size={50} color={theme.colors.red[6]} stroke={1.5} />
            </Dropzone.Reject>
            <Dropzone.Idle>
              <IconCloudUpload size={50} stroke={1.5} className={classes.icon} />
            </Dropzone.Idle>
          </Group>

          <Text ta="center" fw={700} fz="lg" mt="xl">
            <Dropzone.Accept>Drop files here</Dropzone.Accept>
            <Dropzone.Reject> {acceptedFiles} file less than 30mb</Dropzone.Reject>
            <Dropzone.Idle>Upload resume</Dropzone.Idle>
          </Text>

          <Text className={classes.description}>
            Drag&apos;n&apos;drop files here to upload. We can accept only <i>{acceptedFiles}</i> files that
            are less than 30mb in size.
          </Text>
        </div>
      </Dropzone>

      <Button className={classes.control} size="md" radius="xl" onClick={() => openRef.current?.()}>
        Select files
      </Button>
      <div>
        {uploads.map((upload, index) => (
          <FileUploadItem
            key={upload.file.name + index}
            file={upload.file}
            status={upload.status}
            progress={upload.progress}
          />
        ))}
      </div>
    </div>

  );
}