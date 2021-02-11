import React from 'react'
import styled, { keyframes } from 'styled-components'

import theme from '../../assets/theme'

const spinner = keyframes`
  0% {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
    opacity: 0.5;
  }
  100% {
    transform: rotate(360deg);
  }
`

const LoaderStyle = styled('span')`
  display: inline-flex;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  overflow: hidden;
  border-width: 2px;
  border-style: solid;
  border-color: ${(props) => props.loaderColor};
  border-top-color: transparent !important;
  animation: ${spinner} 1s linear infinite;
`

interface LoaderProps {
  loaderColor?: string
  style?: object
}

const Loader = ({ loaderColor = theme.palette.black, ...props }: LoaderProps) => (
  <LoaderStyle loaderColor={loaderColor} {...props} />
)

export default Loader
