package com.example.myapplication

import com.example.lib_annotation.ShareItemAction
import com.example.myapplication.custom_page.CharListFragment

/**
 * @author Quinn
 * @date 2023/7/21
 * @Desc
 */
@ShareItemAction(
    item = "PersonalSpaceShare",
    first = [CharListFragment::class, CharListFragment::class],
    second = [CharListFragment::class, CharListFragment::class]
)
class PersonalSpaceShareItem

@ShareItemAction(
    item = "GuestMomentShare",
    first = [CharListFragment::class, CharListFragment::class],
    second = [CharListFragment::class]
)
class GuestMomentShareItem


@ShareItemAction(
    item = "ARBonShare",
    first = [CharListFragment::class, CharListFragment::class, CharListFragment::class],
    second = [CharListFragment::class]
)
class ARBonShareItem