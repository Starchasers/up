export interface IUploadFileRequest {
  data: {
    file: File
    expires?: number | string
  }
}

export interface IUploadFileResponse {
  key: string
  accessToken: string
  toDelete: string
}
