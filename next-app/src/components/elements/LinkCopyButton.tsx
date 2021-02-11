import styled from '@emotion/styled'
import { css } from '@emotion/css'
import copy from 'copy-to-clipboard'
import React, { useCallback, useRef } from 'react'
import Button from './Button'
import useTooltip from '../../hooks/useTooltip'
import DesktopContainer from './DesktopContainer'
import MobileContainer from './MobileContainer'
import theme from '../../assets/theme'

interface ILinkCopyButton {
  link: string
  text: React.ReactNode
}

const Area = styled('div')`
  position: relative;
  display: grid;
  grid-template-columns: 1fr 50px;

  @media (max-width: ${(props) => props.theme.breakpoints.sm}) {
    grid-template-columns: 1fr;
    grid-row-gap: 10px;
  }
`

const CopyContainer = styled('div')`
  background: #2b2e39;
  border-top-left-radius: 8px;
  border-bottom-left-radius: 8px;
  overflow-x: auto;
  overflow-y: hidden;
  padding: 8px 16px;
  min-height: 32px;
  cursor: pointer;
  transition: 200ms background;

  &:hover {
    background: #1f2229;
  }
`

const LinkText = styled('span')`
  display: flex;
  width: 100%;
  height: 100%;
  color: ${(props) => props.theme.colors.timberwolf};
  align-items: center;
  justify-content: center;
  font-size: 16px;
  line-height: 1;

  @media (min-width: ${(props) => props.theme.breakpoints.md}) and (max-width: ${(props) =>
      props.theme.breakpoints.lg}) {
    justify-content: flex-start;
  }

  @media (max-width: ${(props) => props.theme.breakpoints.sm}) {
    justify-content: flex-start;
  }
`

const LinkCopyButton = (props: ILinkCopyButton) => {
  const [setTooltip, Tooltip] = useTooltip('Copied!')
  const linkNode = useRef(null)

  const handleDesktopCopyButton: React.MouseEventHandler<HTMLDivElement> = useCallback(
    (event) => {
      event.preventDefault()
      setTooltip(true)
      copy(props.link)
    },
    [props.link]
  )

  const handleMobileCopyButton: React.MouseEventHandler<HTMLDivElement> = useCallback(
    (event) => {
      event.preventDefault()
      setTooltip(true)
      copy(props.link)
      setTimeout(() => setTooltip(false), 2000)
    },
    [props.link]
  )

  const selectLink = useCallback(() => {
    try {
      const range = new Range()
      range.setStart(linkNode.current, 0)
      range.setEndAfter(linkNode.current)
      const selection = window.getSelection()
      selection.removeAllRanges()
      selection.addRange(range)
    } catch (e) {
      // Handle warning about browser is outdated to have this feature
      console.log('Your browser is outdated', e)
    }
  }, [linkNode.current])

  const handlePreventClick: React.MouseEventHandler<HTMLAnchorElement> = useCallback((event) => {
    event.preventDefault()
  }, [])

  return (
    <>
      <DesktopContainer breakpoint={theme.breakpoints.sm}>
        <Area>
          <CopyContainer onClick={selectLink}>
            <a href={props.link} onClick={handlePreventClick}>
              <LinkText ref={linkNode}>{props.link}</LinkText>
            </a>
          </CopyContainer>
          <div style={{ position: 'relative', display: 'grid' }}>
            {Tooltip}
            <Button
              variant={'primary'}
              colorStates={{
                unset: 'upPrimary',
                hover: 'upPrimaryHover',
                active: 'upPrimaryActive',
                focus: 'upPrimaryActive',
                disabled: 'upPrimaryActive'
              }}
              buttonProps={{
                title: 'Copy URL',
                className: css`
                  border-top-left-radius: unset;
                  border-bottom-left-radius: unset;
                  opacity: 0.85;
                `,
                onClick: handleDesktopCopyButton,
                onMouseLeave: () => {
                  setTimeout(() => setTooltip(false), 2000)
                }
              }}
            >
              <span
                className={css`
                  svg {
                    margin: unset;
                  }
                `}
              >
                {props.text}
              </span>
            </Button>
          </div>
        </Area>
      </DesktopContainer>
      <MobileContainer breakpoint={theme.breakpoints.sm}>
        <Area>
          <CopyContainer onClick={selectLink}>
            <a href={props.link} onClick={handlePreventClick}>
              <LinkText>{props.link}</LinkText>
            </a>
            {Tooltip}
          </CopyContainer>
          <Button
            variant={'primary'}
            colorStates={{
              unset: 'upPrimary',
              hover: 'upPrimaryHover',
              active: 'upPrimaryActive',
              focus: 'upPrimaryActive',
              disabled: 'upPrimaryActive'
            }}
            buttonProps={{
              className: css`
                height: 26px;
                opacity: 0.85;
              `,
              onClick: handleMobileCopyButton
            }}
          >
            <span
              className={css`
                svg {
                  margin: 0 0 0 10px;
                  font-size: 18px;
                }
              `}
            >
              {props.text}
            </span>
          </Button>
        </Area>
      </MobileContainer>
    </>
  )
}

export default LinkCopyButton
