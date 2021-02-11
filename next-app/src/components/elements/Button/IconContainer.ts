import styled from 'styled-components'

import { changeOpacityOnLoading } from './LoaderContainer'

const IconContainer = styled('div')`
  display: flex;
  justify-content: center;
  align-items: center;
  margin: ${props => props.disableMargin ? '0' : (props.iconAlign === 'left' ? '0 10px 0 0' : '0 0 0 10px')};
  transition: ${props => props.theme.transitions.normal};

  ${props => props.isLoading && changeOpacityOnLoading};
`

export default IconContainer
