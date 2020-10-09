import React from 'react'
import {
  SimpleForm,
  TextInput,
  Edit,
  required,
  RadioButtonGroupInput,
  PasswordInput,
  Toolbar,
  SaveButton,
  DeleteButton
} from 'react-admin'
import { makeStyles } from '@material-ui/core/styles'

const useStyles = makeStyles({
  toolbar: {
    display: 'flex',
    justifyContent: 'space-between'
  }
})

const CustomToolbar = (props) => (
  <Toolbar {...props} classes={useStyles()}>
    <SaveButton undoable={false} />
    <DeleteButton undoable={true} />
  </Toolbar>
)

export default (props) => (
  <Edit
    {...props}
    undoable={false}
  >
    <SimpleForm toolbar={<CustomToolbar/>}>
      <TextInput
        source='id'
        type='text'
        disabled
      />
      <TextInput
        source='username'
        type='text'
        validate={required()}
      />
      <TextInput
        source='email'
        type='email'
      />
      <PasswordInput
        source='password'
        type='password'
      />
      <RadioButtonGroupInput
        source="role"
        choices={[
          { id: 'USER', name: 'User' },
          { id: 'ADMIN', name: 'Admin' },
        ]}
      />
    </SimpleForm>
  </Edit>
)


