import styled, { css } from 'styled-components'
import { theme } from '../../theme'

const space = theme.spacing[2]

const opacity = css`
    &:hover, &:focus {
    opacity: 0.95;
  }
`

const Button = styled('div')`
  margin: -${space} -${space} -${space} 0;
  padding: ${space};
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  right: ${space};
  top: ${space};
  background-color: ${props => !props.transparent && props.theme.colors.primary.three};
  cursor: ${props => props.onClick && 'pointer'};
  transition: 100ms all;
  border-top-right-radius: ${props => props.theme.border.radius};
  border-bottom-right-radius: ${props => props.theme.border.radius};
  width: 60px;
  
  ${props => props.onClick && opacity}
`

export default Button
