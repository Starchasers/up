import { AxiosRequestConfig } from 'axios'

export interface IAxiosRequest {
  config?: AxiosRequestConfig
}

export interface IOnUploadProgress {
  loaded: number
  total: number
}
