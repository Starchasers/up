import styled from 'styled-components'
import { p } from 'styled-components-spacing/dist/cjs'
import { breakpoint } from 'styled-components-breakpoint'

const TextBox = styled('div')`
  position: relative;
  display: flex;
  width: 100%;
  margin: 0 auto;
  border-radius: ${props => props.theme.border.radius};
  ${p(2)};
  padding-right: calc(60px + ${props => props.theme.spacing[2]});
  background-color: ${props => props.theme.colors.secondary.two};
  align-items: center;
  overflow-y: hidden;
  overflow-x: auto;
  transition: 500ms all;
  
  ${breakpoint('sm')`
    justify-content: center;
  `};
`

export default TextBox
