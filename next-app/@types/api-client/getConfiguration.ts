export interface IGetConfigurationRequest {}

export interface IGetConfigurationResponse {
  defaultFileLifetime: number
  maxFileLifetime: number
  maxPermanentFileSize: number
  maxTemporaryFileSize: number
  permanentAllowed: boolean
}
