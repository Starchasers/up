import React from 'react'
import FileUploadProvider from './file-upload-provider'
import { theme } from '../components/theme'
import { ThemeProvider } from 'styled-components'
import GlobalStyles from '../components/globalStyles'
import PageProvider from './page-provider'

const Providers = ({ children }) => (
  <ThemeProvider theme={theme}>
    <GlobalStyles/>
    <PageProvider>
      <FileUploadProvider>
        {children}
      </FileUploadProvider>
    </PageProvider>
  </ThemeProvider>
)

export default Providers
