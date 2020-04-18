import styled from 'styled-components'
import Input from './Input'
import Container from './Container'
import Text from './Text'
import Icon from './Icon'
import DropZone from './DropZone'
import Or from './Or'
import Button from './Button'

const CustomFileUpload = styled('div')`
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  min-height: 300px;
`

CustomFileUpload.Input = Input
CustomFileUpload.Container = Container
CustomFileUpload.Text = Text
CustomFileUpload.Icon = Icon
CustomFileUpload.DropZone = DropZone
CustomFileUpload.Or = Or
CustomFileUpload.Button = Button

export default CustomFileUpload
