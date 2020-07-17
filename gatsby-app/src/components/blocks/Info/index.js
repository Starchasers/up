import styled from 'styled-components'
import { breakpoint } from 'styled-components-breakpoint'
import Popup from './Popup'
import Icon from './Icon'
import Text from './Text'
import TransitionDiv from './TransitionDiv'

const Info = styled('div')`
  position: absolute;
  left: 0;
  display: none;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  
  &:hover {
    > ${TransitionDiv} {
      width: 280px;
      height: 170px;
    }
  }
  
  &::before {
    position: absolute;
    content: '';
    width: 50%;
    height: 60%;
    margin: auto;
    z-index: 1;
  }
  
  ${breakpoint('md')`
    display: flex;
  `}
`

Info.Icon = Icon
Info.Popup = Popup
Info.Text = Text
Info.TransitionDiv = TransitionDiv

export default Info
