import styled, { css } from 'styled-components'
import Container from './Container'
import Text from './Text'

const active = css`
  display: block;
`

const GlobalDropZone = styled('div')`
  display: none;
  position: fixed;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  padding: 8px;
  
  ${props => props.active && active};
`

GlobalDropZone.Container = Container
GlobalDropZone.Text = Text

export default GlobalDropZone
