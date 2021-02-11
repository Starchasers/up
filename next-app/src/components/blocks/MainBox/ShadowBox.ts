import styled from 'styled-components'

const ShadowBox = styled('div')`
  position: absolute;
  z-index: -1;
  width: 100%;
  height: calc(100% + 25px);
  background: ${props => props.theme.palette.secondary};
  border-radius: ${props => props.theme.constants.borderRadius};
  top: 25px;
  left: 25px;
`

export default ShadowBox
