export interface IDeleteFileRequest {
  key: string
  data: {
    accessToken: string
  }
}

export interface IDeleteFileResponse {
  key: string
  accessToken: string
  toDelete?: string
}
