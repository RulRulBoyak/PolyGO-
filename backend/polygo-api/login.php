<?php
require_once __DIR__ . '/config.php';

$input = input_json();
$studentId = trim((string)($input['student_id'] ?? ''));
$password = (string)($input['password'] ?? '');

$query = $pdo->prepare('SELECT id, full_name, student_id, email, mobile, password_hash, is_verified FROM users WHERE student_id = ? LIMIT 1');
$query->execute([$studentId]);
$user = $query->fetch();

if (!$user || !password_verify($password, $user['password_hash'])) respond(false, 'Incorrect student ID or password');

unset($user['password_hash']);
$user['id'] = (int)$user['id'];
$user['name'] = $user['full_name'];
$user['studentId'] = $user['student_id'];
unset($user['full_name'], $user['student_id']);
respond(true, 'Login successful', ['user' => $user]);
