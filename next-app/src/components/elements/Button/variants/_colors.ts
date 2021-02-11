import theme from '../../../../assets/theme'
import { TColor } from '../../../../assets/theme/colors'
import { TPalletColor } from '../../../../assets/theme/palette'

export type possibleColors = TPalletColor | TColor

export interface colorStates {
  // Default color
  unset?: possibleColors
  // Color on Hover
  hover?: possibleColors
  // Color when active
  active?: possibleColors
  // Color when focused
  focus?: possibleColors
  // Color when disabled
  disabled?: possibleColors
}

const colors: { [index in possibleColors]: string } = {
  ...theme.colors,
  ...theme.palette
}

export default colors
