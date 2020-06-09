import styled from 'styled-components'
import { breakpoint } from 'styled-components-breakpoint'
import React from 'react'

const Div = styled('div')`
  position: absolute;
  width: 100%;
  height: calc(100% - ${props => props.theme.spacing[4]});
  background-color: ${props => props.theme.colors.secondary.two};
  z-index: -1;
  border-radius: ${props => props.theme.border.radius};
  opacity: 70%;

  top: ${props => props.theme.spacing[4]};
  left: ${props => props.theme.spacing[4]};

  ${breakpoint('xs', 'sm')`
    display: none;
  `}
`

const Offset = styled('div')`
  height: ${props => props.theme.spacing[4]};
`

const Shadow = (props) => (
  <Offset>
    <Div {...props}/>
  </Offset>
)

export default Shadow
