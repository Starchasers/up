import styled from 'styled-components'

const Shadow = styled('div')`
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: ${props => props.theme.colors.secondary.two};
  z-index: -1;
  border-radius: 8px;

  top: ${props => props.theme.spacing[4]};
  left: ${props => props.theme.spacing[4]};
`

export default Shadow
