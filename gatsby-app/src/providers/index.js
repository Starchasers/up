import React from 'react'
import FileUploadProvider from './file-upload-provider'
import { theme } from '../components/theme'
import { ThemeProvider } from 'styled-components'

const Providers = ({ children }) => (
  <ThemeProvider theme={theme}>
    <FileUploadProvider>
      {children}
    </FileUploadProvider>
  </ThemeProvider>
)

export default Providers
