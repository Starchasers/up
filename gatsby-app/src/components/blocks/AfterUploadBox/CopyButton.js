import styled from 'styled-components'
import { theme } from '../../theme'

const space = theme.spacing[2]

const CopyButton = styled('div')`
  margin: -${space} -${space} -${space} 0;
  padding: ${space};
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  right: ${space};
  top: ${space};
  background-color: ${props => props.theme.colors.primary.three};
  cursor: pointer;
  transition: 100ms all;
  
  &:hover, &:focus {
    opacity: 0.95;
  }
`

export default CopyButton
