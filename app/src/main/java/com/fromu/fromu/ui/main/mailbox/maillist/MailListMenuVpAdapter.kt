package com.fromu.fromu.ui.main.mailbox.maillist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MailListMenuVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SendMailListFragment()
            1 -> ReceiveMailListFragment()
            else -> throw ClassCastException("Unknown position MailListMenuVpAdapter: $position")
        }
    }
}