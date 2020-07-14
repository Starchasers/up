import React from 'react'
import FileUploadProvider from './file-upload-provider'
import { theme } from '../components/theme'
import { ThemeProvider } from 'styled-components'
import GlobalStyles from '../components/globalStyles'

const Providers = ({ children }) => (
  <ThemeProvider theme={theme}>
    <GlobalStyles/>
    <FileUploadProvider>
      {children}
    </FileUploadProvider>
  </ThemeProvider>
)

export default Providers
