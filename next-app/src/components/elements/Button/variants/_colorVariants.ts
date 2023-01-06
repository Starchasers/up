import { TColor } from '../../../../assets/theme/colors'

export type colorStates = {
  unset: TColor
  hover: TColor
  active: TColor
  focus: TColor
  disabled: TColor
}

interface colorVariant {
  [key: string]: colorStates
}

const colorVariants: colorVariant = {
  primary: {
    unset: 'shark',
    hover: 'shark',
    active: 'shark',
    focus: 'shark',
    disabled: 'shark'
  },
  secondary: {
    unset: 'upPositive',
    hover: 'upPositiveHover',
    active: 'upPositiveHover',
    focus: 'upPositiveHover',
    disabled: 'upSuccessBgNight'
  },
  transparent: {
    unset: 'transparent',
    hover: 'transparent',
    active: 'transparent',
    focus: 'transparent',
    disabled: 'transparent'
  },
  fluffy: {
    unset: 'upPrimary',
    hover: 'upPrimaryHover',
    active: 'upPrimaryActive',
    focus: 'upPrimaryActive',
    disabled: 'upPrimaryActive'
  },
  dangerous: {
    unset: 'upNegative',
    hover: 'upNegativeNight',
    active: 'upNegativeHover',
    focus: 'upNegativeHover',
    disabled: 'upErrorBgNight'
  }
}

export default colorVariants
