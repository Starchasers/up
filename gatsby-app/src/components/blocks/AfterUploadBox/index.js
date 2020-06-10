import styled from 'styled-components'
import { pl } from 'styled-components-spacing/dist/cjs'
import TextBox from './TextBox'
import Link from './Link'
import Back from './Back'
import Icon from './Icon'
import CopyButton from './CopyButton'
import Tooltip from './Tooltip'
import Center from './Center'
import Text from './Text'

const AfterUploadBox = styled('div')`
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  min-height: 300px;
  ${pl({md: 4})};
  position: relative;
`

AfterUploadBox.Back = Back
AfterUploadBox.Center = Center
AfterUploadBox.CopyButton = CopyButton
AfterUploadBox.Icon = Icon
AfterUploadBox.Icon = Icon
AfterUploadBox.Link = Link
AfterUploadBox.Text = Text
AfterUploadBox.TextBox = TextBox
AfterUploadBox.Tooltip = Tooltip

export default AfterUploadBox
