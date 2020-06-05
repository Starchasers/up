import styled, { css } from 'styled-components'
import { p } from 'styled-components-spacing/dist/cjs'

const active = css`
  bottom: -30px;
  opacity: 1;
  z-index: 1;
`

const Tooltip = styled('div')`
  position: absolute;
  bottom: 0;
  opacity: 0;
  ${p(1)};
  background-color: ${props => props.theme.colors.secondary.two};
  color: ${props => props.theme.colors.text.one};
  border-radius: ${props => props.theme.border.radius};
  font-size: 15px;
  transition: 200ms all;
  z-index: -1;
  
  ${props => props.active && active};
`

export default Tooltip
