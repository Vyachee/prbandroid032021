<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Contact extends Model
{
    use HasFactory;
    protected $table = "contacts";
    protected $fillable = ['contact_type_id', 'value', 'user_id', 'title'];

    public function type() {
        return $this->hasOne(ContactType::class, 'id', 'contact_type_id');
    }
}
