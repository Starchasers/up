import styled, { css } from 'styled-components'
import Container from './Container'
import Text from './Text'

const active = css`
  display: block;
  animation: display-dropzone 300ms linear;
`

const GlobalDropZone = styled('div')`
  display: none;
  position: fixed;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  padding: 8px;
  
  @keyframes display-dropzone {
    0% {
      opacity: 0;
    }
    100% {
     opacity: 1;
    }
  }
  
  
  ${props => props.active && active};
`

GlobalDropZone.Container = Container
GlobalDropZone.Text = Text

export default GlobalDropZone
