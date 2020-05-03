import styled from 'styled-components'
import { mr, p } from 'styled-components-spacing/dist/cjs'
import Icon from './Icon'

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
  
  &:hover {
    opacity: 100%;
  }
  
  > ${Icon} {
    font-size: 20px;
    ${mr(3)};
  }
`

export default Back
