import styled from 'styled-components'
import { p } from 'styled-components-spacing/dist/cjs'

const Back = styled('div')`
  position: absolute;
  bottom: 0;
  color: ${props => props.theme.colors.text.one};
  background-color: ${props => props.theme.colors.secondary.two};
  ${p(2)};
  font-size: 15px;
  border-radius: ${props => props.theme.border.radius};
  display: flex;
  align-items: center;
  opacity: 80%;
  transition: 300ms all;
  cursor: pointer;
  text-decoration: none;
  text-underline: none;
  
  &:hover {
    opacity: 100%;
  }
`

export default Back
