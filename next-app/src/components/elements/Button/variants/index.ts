import { colorStates } from './_colors'

import colorVariants from './_colorVariants'

import inline from './inline'
import outline from './outline'
import transparent from './transparent'

export interface variantProps {
  color: colorStates
}

export type possibleVariants =
  'inline' |
  'outline' |
  'transparent'

const variants = (props: { colorStates?: colorStates }): { [index in possibleVariants]: { button?: any, text?: any } } => ({
  inline: inline({
    color: {
      ...colorVariants.primary,
      ...props.colorStates
    }
  }),
  outline: outline({
    color: {
      ...colorVariants.secondary,
      ...props.colorStates
    }
  }),
  transparent: transparent({
    color: {
      ...colorVariants.transparent,
      ...props.colorStates
    }
  })
})

export const includeVariant = (props: { colorStates?: colorStates, variant: string }, element: string) => {
  return variants(props)[props.variant][element]
}

export default variants
