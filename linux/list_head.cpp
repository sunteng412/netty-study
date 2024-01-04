//
// Created by st152 on 2024/1/3.
//
#include <stdio.h>
struct list_head{
struct list_head*next,*prev;//双向链表
};

struct bundle{
int i;
struct list_head entry;
}__attribute__((packed));

int main(){
    struct bundle myhead {0};
    struct bundle x ={100};
    struct bundle y ={200};
    struct bundle z ={300};

    struct list_head *head;
    struct list_head *a;
    struct list_head *b;
    struct list_head *c;

    head = &myhead.entry;
    a = &x.entry;
    b = &y.entry;
    c = &z.entry;

    head->prev = c;
    head->next =  a;
    a->prev = head;
    a->next = b;
    b->prev = a;
    b->next = c;
    c->prev = b;
    b->next = head;

    struct list_head *tmp = myhead.entry.next;
    while (tmp != &myhead.entry){
        printf("i :%d\n",((struct bundle *)((void *) tmp - 4))->i);
        tmp = tmp->next;
    }
    return 0;

}