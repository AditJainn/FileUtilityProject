import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import '@mantine/core/styles.css';
import '@mantine/dropzone/styles.css';
import { MantineProvider } from '@mantine/core';


import { DropzoneButton } from './components/DropzoneButton';
import App from './App';
createRoot(document.getElementById('root')!).render(
  <StrictMode>

    <MantineProvider>
      <App/>
      <DropzoneButton />
    </MantineProvider>

  </StrictMode>
);