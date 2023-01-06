import { css } from '@emotion/css'
import React, { useContext, useMemo } from 'react'
import Head from 'next/head'
import Page from '../../blocks/Page'
import { FileUploadContext } from '../../../providers/FileUploadProvider'
import DragContainer from '../DragContainer'
import { UploadBoxContentContext } from '../../../providers/UploadBoxContentProvider'
import { Box } from '../../../../@types/box'

export interface LayoutProps {
  title?: string
}

const Layout: React.FC<LayoutProps> = ({
  title = 'UP | Share your dreams',
  children,
  ...props
}) => {
  const uploadBoxContentContext = useContext(UploadBoxContentContext)
  const fileUploadContext = useContext(FileUploadContext)

  const contentClassname = useMemo(() => {
    return (
      (fileUploadContext.fileUploadDropzone.isDragActive &&
        uploadBoxContentContext.currentBox === Box.DefaultUploadBox &&
        css`
          filter: brightness(50%);
        `) ??
      null
    )
  }, [fileUploadContext.fileUploadDropzone.isDragActive, uploadBoxContentContext.currentBox])

  return (
    <>
      <Head>
        <title>{title}</title>
      </Head>
      <Page>
        <Page.Content
          {...fileUploadContext.fileUploadDropzone?.getRootProps()}
          {...props}
          className={contentClassname}
          children={children}
        />
        <DragContainer />
      </Page>
    </>
  )
}

export default Layout
