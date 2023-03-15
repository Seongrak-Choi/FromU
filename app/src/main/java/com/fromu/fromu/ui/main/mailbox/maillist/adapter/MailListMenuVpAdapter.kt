package com.fromu.fromu.ui.main.mailbox.maillist.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fromu.fromu.ui.main.mailbox.maillist.ReceiveMailListFragment
import com.fromu.fromu.ui.main.mailbox.maillist.SendMailListFragment

class MailListMenuVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReceiveMailListFragment()
            1 -> SendMailListFragment()
            else -> throw ClassCastException("Unknown position MailListMenuVpAdapter: $position")
        }
    }
}