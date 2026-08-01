package com.example.lacantera.navigation

object Routes {

    const val SPLASH = "splash"

    const val PUBLIC_HOME = "public_home"
    const val PROGRAMS = "programs"
    const val RULES = "rules"
    const val PRIVACY = "privacy"
    const val TERMS = "terms"
    const val SUPPORT = "support"

    const val LOGIN = "login"

    const val DASHBOARD = "dashboard"
    const val DASHBOARD_REFEREE = "dashboard_referee"
    const val DASHBOARD_CAPTAIN = "dashboard_captain"

    const val MATCHDAYS = "matchdays"

    const val TEAMS = "teams"

    const val TEAM_DETAIL = "team_detail/{teamId}"

    fun teamDetail(teamId: Int): String {
        return "team_detail/$teamId"
    }
}