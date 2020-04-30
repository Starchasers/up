import styled from 'styled-components'
import { p } from 'styled-components-spacing/dist/cjs'
import { breakpoint } from 'styled-components-breakpoint'

const TextBox = styled('div')`
  display: flex;
  width: 100%;
  margin: 0 auto;
  border-radius: ${props => props.theme.border.radius};
  ${p(2)};
  background-color: ${props => props.theme.colors.secondary.two};
  align-items: center;
  overflow-y: hidden;
  overflow-x: auto;
  
  ${breakpoint('sm')`
    justify-content: center;
  `}
  
`

export default TextBox
