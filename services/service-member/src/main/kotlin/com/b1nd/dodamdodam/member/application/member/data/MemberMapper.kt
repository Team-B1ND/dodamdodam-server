package com.b1nd.dodamdodam.member.application.member.data

import com.b1nd.dodamdodam.member.application.member.data.request.JoinStudentRequest
import com.b1nd.dodamdodam.member.application.member.data.request.JoinTeacherRequest
import com.b1nd.dodamdodam.member.domain.member.entity.MemberEntity
import com.b1nd.dodamdodam.member.domain.student.entity.StudentEntity
import com.b1nd.dodamdodam.member.domain.teacher.entity.TeacherEntity

fun JoinStudentRequest.toMemberEntity(encodedPassword: String): MemberEntity = MemberEntity(
    username = id,
    password = encodedPassword,
    name = name,
    email = email,
    phone = phone,
)

fun JoinStudentRequest.toStudentEntity(member: MemberEntity): StudentEntity = StudentEntity(
    member = member,
    grade = grade,
    room = room,
    number = number,
)

fun JoinTeacherRequest.toMemberEntity(encodedPassword: String): MemberEntity = MemberEntity(
    username = id,
    password = encodedPassword,
    name = name,
    email = email,
    phone = phone,
)

fun JoinTeacherRequest.toTeacherEntity(member: MemberEntity): TeacherEntity = TeacherEntity(
    member = member,
    tel = tel,
    position = position,
)
