import { ThemeProvider } from '@emotion/react'
import React from 'react'
import { AppProps } from 'next/app'
import 'react-toastify/dist/ReactToastify.css'

import GlobalStyle from '../components/elements/Layout/GlobalStyle'
import theme from '../assets/theme'
import Providers from '../components/elements/Layout/Providers'
import { ToastContainer } from 'react-toastify'

const App = ({ Component, pageProps }: AppProps) => (
  <ThemeProvider theme={theme}>
    {GlobalStyle}
    <Providers>
      <Component {...pageProps} />
    </Providers>
    <ToastContainer
      position='top-right'
      autoClose={5000}
      hideProgressBar={true}
      newestOnTop={false}
      closeOnClick
      rtl={false}
      pauseOnFocusLoss
      draggable
      pauseOnHover
    />
  </ThemeProvider>
)

export default App
