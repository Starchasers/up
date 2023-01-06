import { css } from '@emotion/css'
import React from 'react'
import CircularProgressBar from '../../../components/elements/CircularProgressBar'
import { IUseUploadingBoxState } from './useUploadingBox'

const UploadingBoxView = (props: IUseUploadingBoxState) => (
  <div
    className={css`
      display: flex;
      justify-content: center;
      align-items: center;
    `}
  >
    <CircularProgressBar
      value={props.fileUpload.loadingValue ?? 0}
      text={`${
        props.fileUpload.loadingValue === 100 ? 'Saving...' : props.fileUpload.loadingValue ?? 0
      }${(props.fileUpload.loadingValue !== 100 && '%') || ''}`}
      background
    />
  </div>
)

export default UploadingBoxView
