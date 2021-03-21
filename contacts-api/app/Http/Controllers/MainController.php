<?php

namespace App\Http\Controllers;

use App\Models\Contact;
use App\Models\ContactType;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class MainController extends Controller
{
    public function register(Request $request) {
        $validation = Validator::make($request->all(), [
           'login' => 'required|unique:users,login',
           'password' => 'required'
        ]);

        if($validation->fails())
            return response($validation->errors(), 422);

        $user = User::create([
            'login' => $request->login,
            'password' => $request->password,
            'name' => $request->name,
            'token' => Str::random(24)
        ]);

        return response([
            'response' => 'Registration successful',
            'token' => $user->token
        ], 201);
    }

    public function login(Request $request) {
        $validation = Validator::make($request->all(), [
            'login' => 'required|exists:users,login',
            'password' => 'required'
        ]);

        if($validation->fails())
            return response($validation->errors(), 422);

        $user = User::where('login', $request->login)->first();

        if($user->password != $request->password)
            return response([
                'response' => 'Incorrect password'
            ], 403);
        else {
            $user->token = Str::random(24);
            $user->save();

            return response([
                'response' => 'Login successful',
                'token' => $user->token
            ], 200);
        }
    }

    public function createContact(Request $request) {

        $validation = Validator::make($request->all(), [
            'token' => 'required|exists:users,token',
            'contact_type_id' => 'required|exists:contact_types,id',
            'value' => 'required',
            'title' => 'required'
        ]);

        if($validation->fails())
            return response($validation->errors(), 422);

        $user = User::where('token', $request->token)->first();

        $contact = Contact::create([
            'contact_type_id' => $request->contact_type_id,
            'value' => $request->value,
            'user_id' => $user->id,
            'title' => $request->title
        ]);

        return response([
            'response' => 'OK'
        ], 201);

    }

    public function getContacts(Request $request) {


        $validation = Validator::make($request->all(), [
            'token' => 'required|exists:users,token'
        ]);

        if($validation->fails())
            return response($validation->errors(), 422);

        $user = User::where('token', $request->token)->first();
        $contacts = Contact::with('type')->where('user_id', $user->id)->get();

        return response($contacts, 200);
    }

    public function createType(Request $request) {

        $validation = Validator::make($request->all(), [
            'title' => 'required|unique:contact_types,title'
        ]);

        if($validation->fails())
            return response($validation->errors(), 422);

        return response(ContactType::create(['title' => $request->title]), 201);
    }

    public function getTypes() {
        return response(ContactType::select('id', 'title')->get());
    }
}
