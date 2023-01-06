import { DropEvent, FileRejection } from 'react-dropzone'

export type TOnDropFunction = (
  acceptedFiles: File[],
  fileRejections: FileRejection[],
  event: DropEvent
) => void
