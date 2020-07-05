import React from 'react'
import FileUploadProvider from './file-upload-provider'

const Providers = ({ children }) => (
  <FileUploadProvider>
    {children}
  </FileUploadProvider>
)

export default Providers
