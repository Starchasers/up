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

const CustomToolbar = (props) => (
  <Toolbar {...props} style={{ display: 'flex', justifyContent: 'space-between' }}>
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
        validate={required()}
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


