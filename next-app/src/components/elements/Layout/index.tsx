import React, { ReactChild } from 'react'
import Head from 'next/head'

import Providers from './Providers'

import Page from '../../blocks/Page'
import Footer from './Footer'

interface LayoutProps {
  children: ReactChild
  title?: string
}

const Layout = ({ title = 'UP | File Hosting', children, ...props }: LayoutProps) => (
  <Providers>
    <Head>
      <title>{title}</title>
    </Head>
    <Page>
      <Page.Content {...props} children={children} />
      <Footer {...props} />
    </Page>
  </Providers>
)

export default Layout
