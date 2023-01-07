import { css } from '@emotion/css'
import React, { useContext, useMemo } from 'react'
import { FileUploadContext } from '../../../providers/FileUploadProvider'
import Box from './Box'
import BorderContainer from './BorderContainer'
import { TypographyH5 } from '../Typography'
import theme from '../../../assets/theme'
import { UploadBoxContentContext } from '../../../providers/UploadBoxContentProvider'
import { Box as BoxType } from '../../../../@types/box'

const DragContainer = () => {
  const uploadBoxContentContext = useContext(UploadBoxContentContext)
  const fileUploadContext = useContext(FileUploadContext)

  const boxClassname = useMemo(() => {
    return (
      (fileUploadContext.fileUploadDropzone.isDragActive &&
        uploadBoxContentContext.currentBox === BoxType.DefaultUploadBox &&
        css`
          opacity: 1;
          z-index: 100;
        `) ??
      null
    )
  }, [fileUploadContext.fileUploadDropzone.isDragActive, uploadBoxContentContext.currentBox])

  return (
    <Box {...fileUploadContext.fileUploadDropzone?.getRootProps()} className={boxClassname}>
      <BorderContainer>
        <TypographyH5
          className={css`
            color: ${theme.colors.upBase02};
            font-weight: bold;
          `}
        >
          Drop file here!
        </TypographyH5>
      </BorderContainer>
    </Box>
  )
}

export default DragContainer
