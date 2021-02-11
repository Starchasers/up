import React from 'react'
import Link, { LinkProps } from 'next/link'

import { possibleVariants } from './variants'
import { colorStates } from './variants/_colors'

import ButtonText from './ButtonText'
import IconContainer from './IconContainer'
import StyledButton from './StyledButton'
import StyledLink from './StyledLink'
import LoaderContainer from './LoaderContainer'

interface IButtonProps {
  children?: null,
  title?: React.ReactNode,
  icon?: React.ReactNode,
  iconAlign?: 'left' | 'right',
  rounded?: boolean,
  variant?: possibleVariants,
  colorStates?: colorStates,
  link?: LinkProps,
  loader?: React.ReactNode,
  isLoading?: boolean,
  linkProps?: object,
  buttonProps?: object
  textProps?: object,
  disabled?: boolean
}

/**
 * @example
 *   <Button
 *     title='My Awesome title!'
 *     variant='outlined'
 *   />
 **/
const Button = (props: IButtonProps) => {
  const ButtonComponent = () => (
    <StyledButton {...props} {...props.buttonProps}>
      <LoaderContainer isLoading={props.isLoading}>
        {props.loader}
      </LoaderContainer>
      {props.icon && (
        <IconContainer iconAlign={props.iconAlign} disableMargin={!props.title} {...props}>
          {props.icon}
        </IconContainer>
      )}
      {props.title && (
        <ButtonText {...props} {...props.textProps}>
          {props.title}
        </ButtonText>
      )}
    </StyledButton>
  )

  if (props.link) {
    return (
      <Link {...props.link} passHref>
        <StyledLink {...props.linkProps}>
          <ButtonComponent/>
        </StyledLink>
      </Link>
    )
  }

  return <ButtonComponent/>
}

const defaultProps: IButtonProps = {
  title: null,
  icon: null,
  iconAlign: 'right',
  variant: 'inline',
  rounded: false,
  link: undefined,
  loader: null,
  isLoading: false,
  linkProps: {},
  buttonProps: {},
  textProps: {}
}

Button.defaultProps = defaultProps

export default Button
