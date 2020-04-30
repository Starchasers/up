import styled from 'styled-components'
import { my } from 'styled-components-spacing/dist/cjs'

const Or = styled('p')`
  ${my(4)};
  color: ${props => props.theme.colors.text.one};
  font-size: 12px;
  text-align: center;
  position: relative;
  display: inline-grid;
  align-items: center;
  font-weight: bold;
  cursor: default;
  
  &:before, &:after {
    content: '';
    height: 2px;
    width: 35%;
    background-color: ${props => props.theme.colors.text.one};
    display: block;
    position: absolute;
  }
  
  &:before {
    left: 10%;
  }
  &:after {
    right: 10%;
  }
`

export default Or
