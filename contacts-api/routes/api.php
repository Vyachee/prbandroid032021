<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::post('register', [\App\Http\Controllers\MainController::class, 'register']);
Route::post('login', [\App\Http\Controllers\MainController::class, 'login']);
Route::post('createType', [\App\Http\Controllers\MainController::class, 'createType']);
Route::post('createContact', [\App\Http\Controllers\MainController::class, 'createContact']);


Route::get('getTypes', [\App\Http\Controllers\MainController::class, 'getTypes']);
Route::get('getContacts', [\App\Http\Controllers\MainController::class, 'getContacts']);


